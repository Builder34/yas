import { Category } from '../models/Category';

export async function getCategories(): Promise<Category[]> {
  let url = `/api/product/storefront/categories`;
  console.log("==> getCategories url:" + url)
  const response = await fetch(url);
  return await response.json();
}

export async function getCategory(id: number) {
  const response = await fetch(`/api/product/storefront/categories/${id}`);
  return await response.json();
}
