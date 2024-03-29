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
  image: '../../../assets/images/34x34icons.png',
};
