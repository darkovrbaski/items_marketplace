package me.darkovrbaski.items.marketplace.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.ArticleDto;
import me.darkovrbaski.items.marketplace.exception.EntityAlreadyExistsException;
import me.darkovrbaski.items.marketplace.exception.EntityNotFoundException;
import me.darkovrbaski.items.marketplace.mapper.ArticleMapper;
import me.darkovrbaski.items.marketplace.repository.ArticleRepository;
import me.darkovrbaski.items.marketplace.service.intefaces.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ArticleServiceImpl implements ArticleService {

  ArticleRepository articleRepository;
  ArticleMapper articleMapper;

  @Override
  public ArticleDto createArticle(final ArticleDto article) {
    if (articleRepository.existsById(article.id())) {
      throw new EntityAlreadyExistsException("Article already exists.");
    }
    if (articleRepository.existsByName(article.name())) {
      throw new EntityAlreadyExistsException("Article with this name already exists.");
    }
    return articleMapper.toDto(articleRepository.save(articleMapper.toEntity(article)));
  }

  @Override
  public ArticleDto getArticle(final Long id) {
    return articleMapper.toDto(articleRepository.findByIdOrThrow(id));
  }

  @Override
  public ArticleDto updateArticle(final ArticleDto article) {
    if (!articleRepository.existsById(article.id())) {
      throw new EntityNotFoundException("Article not found.");
    }
    if (articleRepository.existsByName(article.name())) {
      throw new EntityAlreadyExistsException("Article with this name already exists.");
    }
    return articleMapper.toDto(articleRepository.save(articleMapper.toEntity(article)));
  }

  @Override
  public void deleteArticle(final Long id) {
    if (!articleRepository.existsById(id)) {
      throw new EntityNotFoundException("Article not found.");
    }
    articleRepository.deleteById(id);
  }

  @Override
  public Page<ArticleDto> getArticles(final int page, final int size) {
    return articleRepository.findAll(PageRequest.of(page, size)).map(articleMapper::toDto);
  }

  @Override
  public Page<ArticleDto> searchArticles(final String name, final int page, final int size) {
    return articleRepository.findByNameContainsIgnoreCase(name, PageRequest.of(page, size))
        .map(articleMapper::toDto);
  }

}
