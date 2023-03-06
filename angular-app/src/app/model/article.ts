export interface Article {
  id: number;
  name: string;
  description: string;
  image: string;
}

export const emptyArticle: Article = {
  id: 0,
  name: 'Article 1',
  description: 'Description 1',
  image: 'https://dy4fqujjkm2dt.cloudfront.net/34x34icons.png',
};
