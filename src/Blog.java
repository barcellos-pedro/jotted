import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Collection(articles = {
        @Article(title = "My First Post",
                slug = "my-first-post",
                description = "This is the description of my first post.",
                author = "John Doe",
                date = "2024-06-01",
                tags = {"introduction", "first post", "blog"},
                image = "https://example.com/image.jpg",
                template = "first.md"),
        @Article(title = "My Second Post",
                slug = "my-second-post",
                description = "This is the description of my second post.",
                author = "Mike Tom",
                date = "2024-06-01",
                tags = {"introduction", "test"},
                image = "https://example.com/image.jpg",
                draft = true,
                template = "second.md"),
        @Article(title = "My Third Post",
                slug = "my-third-post",
                description = "This is the description of my third post.",
                author = "Ana Berkeley",
                date = "2024-06-01",
                tags = {"java"},
                image = "https://example.com/image.jpg",
                template = "third.md")
})
public record Blog(String name) {
    public static Blog of(String name) {
        return new Blog(name);
    }

    void build() {
        Article[] articles = getArticles();

        IO.print("%s has a total of %s articles: ".formatted(name, articles.length));

        Arrays.stream(articles)
                .filter(Blog::isPublished)
                .forEach(Blog::processArticle);
    }

    private static boolean isPublished(Article article) {
        return !article.draft();
    }

    private Article[] getArticles() {
        Collection collection = getClass().getAnnotation(Collection.class);
        return collection.articles();
    }

    private static void processArticle(Article article) {
        var tags = getTags(article);
        var frontMatter = getFrontMatter(article, tags);
        var body = getContent(article);

        // insert front-matter at beginning of file
        body.addFirst(frontMatter);

        writeFinalFile(article, body);
    }

    private static List<String> getContent(Article article) {
        try {
            return Files.readAllLines(Path.of("content", article.template()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFrontMatter(Article article, String tags) {
        return Article.DEFAULT_FRONT_MATTER.formatted(
                article.title(), article.slug(), article.description(),
                article.author(), article.date(), tags,
                article.image(), article.draft(), article.template());
    }

    private static String getTags(Article article) {
        return Arrays.stream(article.tags())
                .map("- %s"::formatted)
                .collect(Collectors.joining("\n  "));
    }

    private static void writeFinalFile(Article article, List<String> content) {
        try {
            Files.write(Path.of("build", article.template()), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}