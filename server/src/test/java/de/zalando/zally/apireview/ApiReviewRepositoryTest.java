package de.zalando.zally.apireview;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static de.zalando.zally.util.TestDateUtil.yesterday;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ApiReviewRepositoryTest {

    @Autowired
    private ApiReviewRepository repository;

    @Before
    public void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    public void shouldFindOneApiReviewRequestFromYesterday() {
        ApiReview reviewRequest = new ApiReview(new ObjectMapper().createObjectNode());
        reviewRequest.setDay(yesterday().toLocalDate());
        reviewRequest.setCreated(yesterday());
        repository.save(reviewRequest);

        assertThat(repository.findAllFromYesterday()).hasSize(1);
    }
}
