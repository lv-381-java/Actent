package com.softserve.actent.repository;

import com.softserve.actent.model.entity.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void whenFindByName_thenReturnCategory() {
        // given
        Category parent = new Category("hobbies", null);
        entityManager.persist(parent);
        entityManager.flush();
        Category category = new Category("dances", parent);
        entityManager.persist(category);
        entityManager.flush();

        // when
        Category found = categoryRepository.findByName(category.getName());

        // then
        assertThat(found.getName())
                .isEqualTo(category.getName());
    }
}
