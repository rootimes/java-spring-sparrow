package sparrow.integration;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sparrow.post.dto.PostRequest;
import sparrow.post.dto.PostSourceRequest;
import sparrow.post.entity.Post;
import sparrow.post.repository.PostRepository;
import sparrow.post.vo.Meta;
import sparrow.post.vo.SourceLink;
import sparrow.post.vo.enums.PostStatus;
import sparrow.post.vo.enums.SourceType;
import sparrow.user.User;
import sparrow.user.UserRepository;

@Transactional
public class PostTest extends RestDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void createPost_shouldReturnCreated() throws Exception {
        User user = createUser();
        String slug = uniqueSlug("slug-1");
        PostRequest request = buildRequest(user.getId(), "title-1", slug, buildMeta("meta-1", "val-1"), null);

        this.mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.title").value("title-1"))
                .andExpect(jsonPath("$.slug").value(slug))
                .andExpect(jsonPath("$.meta['meta-1']").value("val-1"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andDo(document("post-create"));
    }

    @Test
    public void getAllPosts_shouldReturnPagedResult() throws Exception {
        User user = createUser();
        String firstSlug = uniqueSlug("slug-1");
        String secondSlug = uniqueSlug("slug-2");

        createPostEntity(user, "title-1", firstSlug, buildMeta("meta-1", "val-1"), PostStatus.DRAFT);
        createPostEntity(user, "title-2", secondSlug, buildMeta("meta-2", "val-2"), PostStatus.PUBLISHED);

        this.mockMvc.perform(get("/api/posts")
                .param("page", "0")
                .param("size", "20")
                .param("sort", "id,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$.content[?(@.slug == '" + firstSlug + "')].title").value(hasItem("title-1")))
                .andExpect(jsonPath("$.content[?(@.slug == '" + secondSlug + "')].title").value(hasItem("title-2")))
                .andDo(document("post-list"));
    }

    @Test
    public void getPostById_shouldReturnPost() throws Exception {
        User user = createUser();
        String slug = uniqueSlug("slug-1");
        Post created = createPostEntity(user, "title-1", slug, buildMeta("meta-1", "val-1"),
                PostStatus.PUBLISHED);

        this.mockMvc.perform(get("/api/posts/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.title").value("title-1"))
                .andExpect(jsonPath("$.slug").value(slug))
                .andExpect(jsonPath("$.meta['meta-1']").value("val-1"))
                .andExpect(jsonPath("$.status").value("PUBLISHED"))
                .andDo(document("post-get"));
    }

    @Test
    public void getPostBySlug_shouldReturnPost() throws Exception {
        User user = createUser();
        String slug = uniqueSlug("slug-1");
        Post created = createPostEntity(user, "title-1", slug, buildMeta("meta-1", "val-1"),
                PostStatus.PUBLISHED);

        this.mockMvc.perform(get("/api/posts/{slug}", slug))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.title").value("title-1"))
                .andExpect(jsonPath("$.slug").value(slug))
                .andExpect(jsonPath("$.meta['meta-1']").value("val-1"))
                .andExpect(jsonPath("$.status").value("PUBLISHED"))
                .andDo(document("post-get"));
    }

    @Test
    public void updatePost_shouldReturnUpdatedPost() throws Exception {
        User user = createUser();
        Post created = createPostEntity(user, "title-1", uniqueSlug("slug-1"), buildMeta("meta-1", "val-1"),
                PostStatus.DRAFT);

        String updatedSlug = uniqueSlug("slug-2");
        PostRequest updateRequest = buildRequest(user.getId(), "title-2", updatedSlug,
                buildMeta("meta-2", "val-2"), PostStatus.PUBLISHED);

        this.mockMvc.perform(put("/api/posts/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.title").value("title-2"))
                .andExpect(jsonPath("$.slug").value(updatedSlug))
                .andExpect(jsonPath("$.meta['meta-2']").value("val-2"))
                .andExpect(jsonPath("$.status").value("PUBLISHED"))
                .andDo(document("post-update"));
    }

    @Test
    public void deletePost_shouldRemovePost() throws Exception {
        User user = createUser();
        Post created = createPostEntity(user, "title-1", uniqueSlug("slug-1"), null, PostStatus.DRAFT);

        this.mockMvc.perform(delete("/api/posts/{id}", created.getId()))
                .andExpect(status().isNoContent())
                .andDo(document("post-delete"));

        this.mockMvc.perform(get("/api/posts/{id}", created.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Post not found"));
    }

    @Test
    public void createPost_withSourceAndTags_shouldReturnCreated() throws Exception {
        User user = createUser();
        String slug = uniqueSlug("slug-1");
        PostRequest request = buildRequest(user.getId(), "title-1", slug, buildMeta("meta-1", "val-1"), null);
        request.setSource(buildSource(SourceType.MARKDOWN, "src-key-1", "src-val-1"));
        request.setTags(Set.of("tag-1", "tag-2"));

        this.mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.meta['meta-1']").value("val-1"))
                .andExpect(jsonPath("$.source.source_type").value("MARKDOWN"))
                .andExpect(jsonPath("$.source.source_link.src-key-1").value("src-val-1"))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasItem("tag-1")))
                .andExpect(jsonPath("$.tags", hasItem("tag-2")))
                .andDo(document("post-create-with-source-tags"));
    }

    @Test
    public void updatePost_withTags_shouldReplaceTags() throws Exception {
        User user = createUser();
        Post created = createPostEntity(user, "title-1", uniqueSlug("slug-1"),
                buildMeta("meta-1", "val-1"), PostStatus.DRAFT);

        String updatedSlug = uniqueSlug("slug-2");
        PostRequest updateRequest = buildRequest(user.getId(), "title-2", updatedSlug,
                buildMeta("meta-2", "val-2"), PostStatus.PUBLISHED);
        updateRequest.setTags(Set.of("tag-2", "tag-3"));

        this.mockMvc.perform(put("/api/posts/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta['meta-2']").value("val-2"))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasItem("tag-2")))
                .andExpect(jsonPath("$.tags", hasItem("tag-3")))
                .andDo(document("post-update-with-tags"));
    }

    @Test
    public void createPost_withSameTag_shouldReuseExistingTag() throws Exception {
        User user = createUser();

        PostRequest first = buildRequest(user.getId(), "title-1", uniqueSlug("slug-1"), null, null);
        first.setTags(Set.of("tag-1"));
        this.mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(first)))
                .andExpect(status().isCreated());

        PostRequest second = buildRequest(user.getId(), "title-2", uniqueSlug("slug-2"), null, null);
        second.setTags(Set.of("tag-1"));
        this.mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(second)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tags", hasItem("tag-1")))
                .andDo(document("post-create-reuse-tag"));
    }

    @Test
    public void updatePost_removeAllTags_shouldRemoveTags() throws Exception {
        User user = createUser();
        Post created = createPostEntity(user, "title-1", uniqueSlug("slug-1"), null, PostStatus.DRAFT);

        PostRequest setupReq = buildRequest(user.getId(), "title-1", created.getSlug(), null, PostStatus.DRAFT);
        setupReq.setTags(Set.of("tag-1", "tag-2"));
        this.mockMvc.perform(put("/api/posts/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(setupReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags.length()").value(2));

        PostRequest removeReq = buildRequest(user.getId(), "title-1", created.getSlug(), null, PostStatus.DRAFT);
        removeReq.setTags(Set.of());

        this.mockMvc.perform(put("/api/posts/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(removeReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags.length()").value(0));
    }

    @Test
    public void updatePost_partialTagsUpdate_shouldUpdateTags() throws Exception {
        User user = createUser();
        Post created = createPostEntity(user, "title-1", uniqueSlug("slug-1"), null, PostStatus.DRAFT);

        PostRequest setupReq = buildRequest(user.getId(), "title-1", created.getSlug(), null, PostStatus.DRAFT);
        setupReq.setTags(Set.of("tag-1", "tag-2"));
        this.mockMvc.perform(put("/api/posts/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(setupReq)))
                .andExpect(status().isOk());

        PostRequest updateReq = buildRequest(user.getId(), "title-1", created.getSlug(), null, PostStatus.DRAFT);
        updateReq.setTags(Set.of("tag-2", "tag-3"));

        this.mockMvc.perform(put("/api/posts/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags.length()").value(2))
                .andExpect(jsonPath("$.tags", hasItem("tag-2")))
                .andExpect(jsonPath("$.tags", hasItem("tag-3")))
                .andExpect(jsonPath("$.tags", org.hamcrest.Matchers.not(hasItem("tag-1"))));
    }

    @Test
    public void updatePost_addSourceToPostWithoutSource_shouldSucceed() throws Exception {
        User user = createUser();
        Post created = createPostEntity(user, "title-1", uniqueSlug("slug-1"), null, PostStatus.DRAFT);

        PostRequest updateReq = buildRequest(user.getId(), "title-2", created.getSlug(), null, PostStatus.DRAFT);
        updateReq.setSource(buildSource(SourceType.MARKDOWN, "src-key-1", "src-val-1"));

        this.mockMvc.perform(put("/api/posts/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").exists())
                .andExpect(jsonPath("$.source.source_type").value("MARKDOWN"))
                .andExpect(jsonPath("$.source.source_link.src-key-1").value("src-val-1"));
    }

    @Test
    public void updatePost_updateExistingSource_shouldUpdateSource() throws Exception {
        User user = createUser();
        String slug = uniqueSlug("slug-1");
        PostRequest createReq = buildRequest(user.getId(), "title-1", slug, null, PostStatus.DRAFT);
        createReq.setSource(buildSource(SourceType.MARKDOWN, "src-key-1", "src-val-1"));

        String responseStr = this.mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        int postId = objectMapper.readTree(responseStr).get("id").asInt();

        PostRequest updateReq = buildRequest(user.getId(), "title-1", slug, null, PostStatus.DRAFT);
        updateReq.setSource(buildSource(SourceType.MARKDOWN, "src-key-1", "src-val-2"));

        this.mockMvc.perform(put("/api/posts/{id}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source.source_link.src-key-1").value("src-val-2"));
    }

    @Test
    public void createPost_withDuplicateSlug_shouldReturnConflict() throws Exception {
        User user = createUser();
        String slug = "slug-1";
        createPostEntity(user, "title-1", slug, null, PostStatus.PUBLISHED);

        PostRequest request = buildRequest(user.getId(), "title-2", slug, null, null);

        this.mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Slug already exists: slug-1"));
    }

    @Test
    public void updatePost_withDuplicateSlug_shouldReturnConflict() throws Exception {
        User user = createUser();
        createPostEntity(user, "title-1", "slug-1", null, PostStatus.PUBLISHED);
        Post post2 = createPostEntity(user, "title-2", "slug-2", null, PostStatus.PUBLISHED);

        PostRequest updateReq = buildRequest(user.getId(), "title-3", "slug-1", null, PostStatus.PUBLISHED);

        this.mockMvc.perform(put("/api/posts/{id}", post2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Slug already exists: slug-1"));
    }

    private User createUser() {
        String token = UUID.randomUUID().toString().replace("-", "");

        User user = new User();
        user.setName("user_" + token.substring(0, 12));
        user.setPassword("password123");
        user.setEmail("user_" + token.substring(0, 12) + "@example.com");
        user.setDescription("integration test user");

        return userRepository.save(user);
    }

    private Post createPostEntity(User user, String title, String slug, Meta meta, PostStatus status) {
        Post post = new Post();
        post.setUser(user);
        post.setTitle(title);
        post.setSlug(slug);
        post.setMeta(meta);
        post.setStatus(status);
        return postRepository.save(post);
    }

    private String uniqueSlug(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private Meta buildMeta(String key, String value) {
        Meta meta = new Meta();
        meta.setAdditionalProperty(key, value);
        return meta;
    }

    private PostSourceRequest buildSource(SourceType type, String linkKey, String linkValue) {
        SourceLink sourceLink = new SourceLink();
        sourceLink.setAdditionalProperty(linkKey, linkValue);
        PostSourceRequest sourceRequest = new PostSourceRequest();
        sourceRequest.setSourceType(type);
        sourceRequest.setSourceLink(sourceLink);
        return sourceRequest;
    }

    private PostRequest buildRequest(Integer userId, String title, String slug, Meta meta, PostStatus status) {
        PostRequest request = new PostRequest();
        request.setUserId(userId);
        request.setTitle(title);
        request.setSlug(slug);
        request.setMeta(meta);
        request.setStatus(status);
        return request;
    }
}
