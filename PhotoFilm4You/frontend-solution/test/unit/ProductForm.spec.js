import { mount } from '@vue/test-utils';
import ProductForm from '@/views/ProductForm.vue';
import flushPromises from 'flush-promises';
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';

const { getCategoriesMock, getProductsMock, createProductMock } = vi.hoisted(() => ({
  getCategoriesMock: vi.fn(),
  getProductsMock: vi.fn(),
  createProductMock: vi.fn(),
}));

vi.mock('@/services/product/api', () => ({
  default: {
    getProducts: getProductsMock,
    createProduct: createProductMock,
  },
}));

vi.mock('@/services/category/api', () => ({
  default: {
    getCategories: getCategoriesMock,
  },
}));

describe('ProductForm', () => {
  const categories = [
    { id: 1, name: 'Cámaras', parentId: null },
    { id: 2, name: 'Reflex', parentId: 1 },
    { id: 3, name: 'Accesorios', parentId: null },
  ];

  beforeEach(() => {
    getCategoriesMock.mockReset();
    getProductsMock.mockReset();
    createProductMock.mockReset();
  });

  afterEach(() => {
    vi.clearAllTimers();
    vi.useRealTimers();
  });

  it('loads of categories and selection of subcategory', async () => {
    vi.useFakeTimers();
    getCategoriesMock.mockResolvedValue({ data: categories });
    getProductsMock.mockResolvedValue({ data: [] });

    const wrapper = mount(ProductForm);

    await flushPromises();

    const categorySelects = wrapper.findAll('select');
    expect(categorySelects).toHaveLength(1);

    await categorySelects[0].setValue(1);
    await flushPromises();

    const updatedSelects = wrapper.findAll('select');
    expect(updatedSelects).toHaveLength(2);

    await updatedSelects[1].setValue(2);
    await flushPromises();

    expect(wrapper.vm.product.categoryId).toBe(2);
  });

  it('submits the form and calls the API', async () => {
    vi.useFakeTimers();
    getCategoriesMock.mockResolvedValue({ data: categories });
    getProductsMock.mockResolvedValue({ data: [] });
    createProductMock.mockResolvedValue({ data: 10 });

    const wrapper = mount(ProductForm);
    await flushPromises();

    const inputs = wrapper.findAll('input');
    await inputs[0].setValue('Cámara deportiva');
    await inputs[1].setValue('GoPro');
    await inputs[2].setValue('Hero 10');
    await inputs[3].setValue('Ideal para aventura');
    await inputs[4].setValue('35');

    const rootSelect = wrapper.find('select');
    await rootSelect.setValue(1);
    await flushPromises();

    const subSelect = wrapper.findAll('select')[1];
    await subSelect.setValue(2);
    await flushPromises();

    getProductsMock.mockResolvedValue({ data: [{ id: 10, name: 'Cámara deportiva' }] });

    await wrapper.find('form').trigger('submit.prevent');
    await flushPromises();

    expect(createProductMock).toHaveBeenCalledTimes(1);
    expect(createProductMock).toHaveBeenCalledWith({
      id: null,
      name: 'Cámara deportiva',
      brand: 'GoPro',
      model: 'Hero 10',
      description: 'Ideal para aventura',
      dailyPrice: 35,
      categoryId: 2,
    });

    expect(wrapper.vm.message).toBe('Producto creado correctamente.');
    expect(wrapper.vm.product).toEqual({
      id: null,
      name: '',
      brand: '',
      model: '',
      description: '',
      dailyPrice: '',
      categoryId: '',
    });
  });
});