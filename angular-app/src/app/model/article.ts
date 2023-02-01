import { QuantityType } from './quantityType';

export interface Article {
  id: number;
  name: string;
  description: string;
  image: string;
  quantityType: QuantityType;
}

export const emptyArticle: Article = {
  id: 0,
  name: 'Article 1',
  description: 'Description 1',
  image: 'https://picsum.photos/80',
  quantityType: QuantityType.DECIMAL,
};
