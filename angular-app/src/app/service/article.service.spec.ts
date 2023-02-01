import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { ArticleService } from './article.service';

describe('ArticleService', () => {
  let service: ArticleService;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientModule] });
    service = TestBed.inject(ArticleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
