import { ProductSlug } from '../../cart/models/ProductSlug';
import { ProductDetail } from '../models/ProductDetail';
import { ProductAll, ProductFeature } from '../models/ProductFeature';
import { ProductOptionValueGet } from '../models/ProductOptionValueGet';
import { ProductVariation } from '../models/ProductVariation';
import { ProductsGet } from '../models/ProductsGet';

export async function getFeaturedProducts(pageNo: number): Promise<ProductFeature> {
  const res = await fetch(`/api/product/storefront/products/featured?pageNo=${pageNo}`);
  console.log(res)
  if (res.status >= 200 && res.status < 300) return res.json();
  return Promise.reject(res);
}

export async function getProductDetail(slug: string): Promise<ProductDetail> {
  const res = await fetch(`/api/product/storefront/product/${slug}`);
  console.log(res)
  if (res.status >= 200 && res.status < 300) return res.json();
  return Promise.reject(res);
}

export async function getProductOptionValues(productId: number): Promise<ProductOptionValueGet[]> {
  const res = await fetch(
    `/api/product/storefront/product-option-values/${productId}`
  );
  if (res.status >= 200 && res.status < 300) return res.json();
  return Promise.reject(res);
}

export async function getProductByMultiParams(queryString: string): Promise<ProductAll> {
  const res = await fetch(`/api/product/storefront/products?${queryString}`);
  if (res.status >= 200 && res.status < 300) return res.json();
  return Promise.reject(res);
}

export async function getProductVariationsByParentId(
  parentId: number
): Promise<ProductVariation[]> {
  const res = await fetch(
    `/api/product/storefront/product-variations/${parentId}`
  );
  if (res.status >= 200 && res.status < 300) return res.json();
  return Promise.reject(res);
}

export async function getProductSlug(productId: number): Promise<ProductSlug> {
  const res = await fetch(`/api/product/storefront/productions/${productId}/slug`);
  if (res.status >= 200 && res.status < 300) return res.json();
  return Promise.reject(res);
}

export async function getRelatedProductsByProductId(productId: number): Promise<ProductsGet> {
  const res = await fetch(`/api/product/storefront/products/related-products/${productId}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
  if (res.status >= 200 && res.status < 300) return res.json();
  return Promise.reject(res);
}
