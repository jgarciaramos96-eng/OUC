import { mount } from '@vue/test-utils';
import ProductForm from '@/components/ProductForm.vue';
import flushPromises from 'flush-promises';
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';

const { getCategoriesMock, createProductMock } = vi.hoisted(() => ({
  getCategoriesMock: vi.fn(),
  createProductMock: vi.fn(),
}));

vi.mock('@/services/product/api', () => ({
  default: {
    getCategories: getCategoriesMock,
    createProduct: createProductMock,
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
    createProductMock.mockReset();
  });

  afterEach(() => {
    vi.clearAllTimers();
    vi.useRealTimers();
  });

  it('loads of categories and selection of subcategory', async () => {
    vi.useFakeTimers();
    getCategoriesMock.mockResolvedValue({ data: categories });

    const wrapper = mount(ProductForm);

    await flushPromises();

    const categorySelects = wrapper.findAll('select');
    expect(categorySelects).toHaveLength(1);

    await categorySelects[0].setValue('1');
    await flushPromises();

    const updatedSelects = wrapper.findAll('select');
    expect(updatedSelects).toHaveLength(2);

    await updatedSelects[1].setValue('2');
    await flushPromises();

    expect(wrapper.vm.product.categoryId).toBe(2);
  });

  it('submits the form and calls the API', async () => {
    vi.useFakeTimers();
    getCategoriesMock.mockResolvedValue({ data: categories });
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
    await rootSelect.setValue('1');
    await flushPromises();

    const subSelect = wrapper.findAll('select')[1];
    await subSelect.setValue('2');
    await flushPromises();

    await wrapper.find('form').trigger('submit.prevent');
    await flushPromises();

    expect(createProductMock).toHaveBeenCalledTimes(1);
    expect(createProductMock).toHaveBeenCalledWith({
      name: 'Cámara deportiva',
      brand: 'GoPro',
      model: 'Hero 10',
      description: 'Ideal para aventura',
      dailyPrice: 35,
      categoryId: 2,
    });

    expect(wrapper.vm.message).toBe('Producto creado correctamente.');
    expect(wrapper.vm.product).toEqual({
      name: '',
      brand: '',
      model: '',
      description: '',
      dailyPrice: '',
      categoryId: '',
    });
  });
});