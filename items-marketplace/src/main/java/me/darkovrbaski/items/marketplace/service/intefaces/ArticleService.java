package me.darkovrbaski.items.marketplace.service.intefaces;

import me.darkovrbaski.items.marketplace.dto.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {

  ArticleDto createArticle(ArticleDto article);

  ArticleDto getArticle(Long id);

  ArticleDto getArticle(String name);

  ArticleDto updateArticle(ArticleDto article);

  void deleteArticle(Long id);

  Page<ArticleDto> getArticles(int page, int size);

  Page<ArticleDto> searchArticles(String name, int page, int size);

  void updateImage(MultipartFile file, String name);

}
