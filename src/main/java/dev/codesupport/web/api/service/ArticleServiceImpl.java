package dev.codesupport.web.api.service;

import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final CrudOperations<ArticleEntity, Article, Long> crudOperations;

    @Autowired
    public ArticleServiceImpl(
            ArticleRepository articleRepository
    ) {
        crudOperations = new CrudOperations<>(articleRepository, ArticleEntity.class, Article.class);
    }

    @Override
    public List<Article> findAllArticles() {
        return crudOperations.getAll();
    }

    @Override
    public Article getArticleById(Long id) {
        return crudOperations.getById(id);
    }

    @Override
    public Article createArticle(Article article) {
        return crudOperations.createEntity(article);
    }
}
