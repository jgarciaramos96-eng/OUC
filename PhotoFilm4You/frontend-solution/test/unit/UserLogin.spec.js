import { mount } from '@vue/test-utils';
import flushPromises from 'flush-promises';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import UserLogin from '@/components/UserLogin.vue';

const { loginMock } = vi.hoisted(() => ({
  loginMock: vi.fn(),
}));

vi.mock('@/services/user/api', () => ({
  default: {
    login: loginMock,
  },
}));

describe('UserLogin', () => {
  beforeEach(() => {
    loginMock.mockReset();
  });

  const fillCredentials = async (wrapper, email = 'user@example.com', password = 'secret') => {
    const inputs = wrapper.findAll('input');
    await inputs[0].setValue(email);
    await inputs[1].setValue(password);
  };

  it('submits credentials and shows welcome message on success', async () => {
    loginMock.mockResolvedValue({
      data: { fullName: 'Juan Pérez' },
    });

    const wrapper = mount(UserLogin);
    await fillCredentials(wrapper);

    await wrapper.find('form').trigger('submit.prevent');
    await flushPromises();

    expect(loginMock).toHaveBeenCalledTimes(1);
    expect(loginMock).toHaveBeenCalledWith({
      email: 'user@example.com',
      password: 'secret',
    });

    const alert = wrapper.find('.alert');
    expect(alert.exists()).toBe(true);
    expect(alert.text()).toBe('Bienvenido, Juan Pérez');
    expect(alert.classes()).toContain('alert-success');
  });

  it('shows an error message when the API rejects the login', async () => {
    loginMock.mockRejectedValue(new Error('Invalid credentials'));

    const wrapper = mount(UserLogin);
    await fillCredentials(wrapper, 'bad@example.com', 'wrong');

    await wrapper.find('form').trigger('submit.prevent');
    await flushPromises();

    expect(loginMock).toHaveBeenCalledTimes(1);
    expect(loginMock).toHaveBeenCalledWith({
      email: 'bad@example.com',
      password: 'wrong',
    });

    const alert = wrapper.find('.alert');
    expect(alert.exists()).toBe(true);
    expect(alert.text()).toBe('Credenciales incorrectas. Revisa el email y la contraseña.');
    expect(alert.classes()).toContain('alert-danger');
  });
});
