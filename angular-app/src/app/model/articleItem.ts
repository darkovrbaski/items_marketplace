import { Article, emptyArticle } from './article';

export interface ArticleItem {
  quantity: number;
  article: Article;
}

export const emptyArticleItem: ArticleItem = {
  quantity: 0,
  article: emptyArticle,
};
