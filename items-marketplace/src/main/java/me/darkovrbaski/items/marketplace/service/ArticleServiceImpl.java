package me.darkovrbaski.items.marketplace.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.ArticleDto;
import me.darkovrbaski.items.marketplace.exception.EntityAlreadyExistsException;
import me.darkovrbaski.items.marketplace.exception.EntityIntegrityViolationException;
import me.darkovrbaski.items.marketplace.exception.EntityNotFoundException;
import me.darkovrbaski.items.marketplace.mapper.ArticleMapper;
import me.darkovrbaski.items.marketplace.model.Article;
import me.darkovrbaski.items.marketplace.model.S3ImageDir;
import me.darkovrbaski.items.marketplace.repository.ArticleRepository;
import me.darkovrbaski.items.marketplace.service.intefaces.ArticleService;
import me.darkovrbaski.items.marketplace.service.intefaces.ImageService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ArticleServiceImpl implements ArticleService {

  ArticleRepository articleRepository;
  ArticleMapper articleMapper;
  ImageService imageService;

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
  public ArticleDto getArticle(final String name) {
    return articleMapper.toDto(articleRepository.findByName(name));
  }

  @Override
  public ArticleDto updateArticle(final ArticleDto article) {
    final Article existArticle = articleRepository.findByIdOrThrow(article.id());
    if (articleRepository.existsByName(article.name())
        && !existArticle.getName().equals(article.name())) {
      throw new EntityAlreadyExistsException("Article with this name already exists.");
    }
    existArticle.setName(article.name());
    existArticle.setDescription(article.description());
    return articleMapper.toDto(articleRepository.save(existArticle));
  }

  @Override
  public void deleteArticle(final Long id) {
    if (!articleRepository.existsById(id)) {
      throw new EntityNotFoundException("Article not found.");
    }
    try {
      articleRepository.deleteById(id);
    } catch (final DataIntegrityViolationException e) {
      throw new EntityIntegrityViolationException("Article is in use by users, can't be deleted.");
    }
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

  @Override
  public void updateImage(final MultipartFile file, final String name) {
    final var article = articleRepository.findByName(name);
    String imageName = article.getName() + "_" + file.getOriginalFilename();
    imageName = imageName.replaceAll("[^a-zA-Z0-9.]", "_");
    article.setImage(imageName);
    imageService.saveImage(file, imageName, S3ImageDir.PUBLIC_IMAGES);
    articleRepository.save(article);
  }

}
