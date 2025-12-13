import { mount } from '@vue/test-utils';
import flushPromises from 'flush-promises';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import Login from '@/views/Login.vue';

const loginMock = vi.fn();
const jwtDecodeMock = vi.fn();

vi.mock('@/services/user/api', () => ({
  default: {
    login: (...args) => loginMock(...args),
  },
}));

vi.mock('jwt-decode', () => ({
  jwtDecode: (...args) => jwtDecodeMock(...args),
}));

const fillCredentials = async (wrapper, email = 'user@example.com', password = 'secret') => {
  const inputs = wrapper.findAll('input');
  await inputs[0].setValue(email);
  await inputs[1].setValue(password);
};

describe('Login', () => {
  beforeEach(() => {
    loginMock.mockReset();
    jwtDecodeMock.mockReset();
    localStorage.clear();
  });

  it('submits credentials, guarda el token y redirige al home de usuario por defecto', async () => {
    loginMock.mockResolvedValue({
      data: { fullName: 'Juan Perez', token: 'token-user' },
    });
    jwtDecodeMock.mockReturnValue({ role: 'user' });

    const pushMock = vi.fn();
    const wrapper = mount(Login, {
      global: {
        mocks: {
          $router: { push: pushMock },
          $route: { query: {} },
        },
      },
    });

    await fillCredentials(wrapper);

    vi.useFakeTimers();
    await wrapper.find('form').trigger('submit.prevent');
    await flushPromises();

    expect(loginMock).toHaveBeenCalledWith({ email: 'user@example.com', password: 'secret' });
    expect(localStorage.getItem('token')).toBe('token-user');

    const alert = wrapper.find('.alert');
    expect(alert.exists()).toBe(true);
    expect(alert.text()).toBe('Bienvenido, Juan Perez');
    expect(alert.classes()).toContain('alert-success');

    vi.runAllTimers();
    expect(pushMock).toHaveBeenCalledWith('/home');
    vi.useRealTimers();
  });

  it('redirige al path indicado en redirect cuando existe', async () => {
    loginMock.mockResolvedValue({
      data: { fullName: 'Admin', token: 'token-admin' },
    });
    jwtDecodeMock.mockReturnValue({ role: 'admin' });

    const pushMock = vi.fn();
    const wrapper = mount(Login, {
      global: {
        mocks: {
          $router: { push: pushMock },
          $route: { query: { redirect: '/product/add' } },
        },
      },
    });

    vi.useFakeTimers();
    await fillCredentials(wrapper, 'admin@example.com', 'secret');
    await wrapper.find('form').trigger('submit.prevent');
    await flushPromises();
    vi.runAllTimers();

    expect(pushMock).toHaveBeenCalledWith('/product/add');
    vi.useRealTimers();
  });

  it('muestra un mensaje de error cuando el API rechaza el login', async () => {
    loginMock.mockRejectedValue(new Error('Invalid credentials'));
    const pushMock = vi.fn();
    const wrapper = mount(Login, {
      global: {
        mocks: {
          $router: { push: pushMock },
          $route: { query: {} },
        },
      },
    });

    await fillCredentials(wrapper, 'bad@example.com', 'wrong');

    await wrapper.find('form').trigger('submit.prevent');
    await flushPromises();

    expect(loginMock).toHaveBeenCalledWith({
      email: 'bad@example.com',
      password: 'wrong',
    });

    const alert = wrapper.find('.alert');
    expect(alert.exists()).toBe(true);
    expect(alert.text()).toBe('Credenciales incorrectas. Revisa el email y la contraseña.');
    expect(alert.classes()).toContain('alert-danger');
    expect(pushMock).not.toHaveBeenCalled();
  });
});
