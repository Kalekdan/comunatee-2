package com.comunatee.config;

import com.comunatee.entity.Comment;
import com.comunatee.entity.Community;
import com.comunatee.entity.Post;
import com.comunatee.entity.User;
import com.comunatee.repository.CommunityRepository;
import com.comunatee.repository.PostRepository;
import com.comunatee.repository.UserRepository;
import com.comunatee.service.CommentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MockDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final PostRepository postRepository;
    private final CommentService commentService;

    public MockDataInitializer(
            UserRepository userRepository,
            CommunityRepository communityRepository,
            PostRepository postRepository,
            CommentService commentService
    ) {
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
        this.postRepository = postRepository;
        this.commentService = commentService;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("ava").isPresent()) {
            return;
        }

        User ava = createUser("ava", "hash_ava", "https://i.pravatar.cc/150?img=11", 184, 267);
        User marcus = createUser("marcus", "hash_marcus", "https://i.pravatar.cc/150?img=12", 129, 91);
        User priya = createUser("priya", "hash_priya", "https://i.pravatar.cc/150?img=32", 242, 318);
        User noah = createUser("noah", "hash_noah", "https://i.pravatar.cc/150?img=52", 98, 144);

        Community javaJunction = createCommunity(
                "java-junction",
                "Spring, JVM tooling, and practical backend patterns.",
                ava,
                1840
        );
        Community cityTable = createCommunity(
                "city-table",
                "Recipes, food finds, and hosting ideas for busy people.",
                priya,
                960
        );
        Community weekendBuilders = createCommunity(
                "weekend-builders",
                "Small DIY projects, garage fixes, and ambitious Sunday plans.",
                marcus,
                1275
        );

        createJavaPosts(javaJunction, ava, marcus, priya, noah);
        createFoodPosts(cityTable, ava, marcus, priya, noah);
        createBuilderPosts(weekendBuilders, ava, marcus, priya, noah);
    }

    private User createUser(String username, String passwordHash, String profilePicUrl, int postRating, int commentRating) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setProfilePicUrl(profilePicUrl);
        user.setPostRating(postRating);
        user.setCommentRating(commentRating);
        return userRepository.save(user);
    }

    private Community createCommunity(String name, String description, User creator, int subscriberCount) {
        Community community = new Community();
        community.setName(name);
        community.setDescription(description);
        community.setCreator(creator);
        community.setSubscriberCount(subscriberCount);
        return communityRepository.save(community);
    }

    private Post createPost(
            Community community,
            User author,
            String title,
            String body,
            int score,
            LocalDateTime createdAt
    ) {
        Post post = new Post();
        post.setCommunity(community);
        post.setAuthor(author);
        post.setTitle(title);
        post.setBody(body);
        post.setScore(score);
        post.setCreatedAt(createdAt);
        post.setCommentCount(0);
        return postRepository.save(post);
    }

    private void createJavaPosts(Community community, User ava, User marcus, User priya, User noah) {
        Post p1 = createPost(
                community,
                ava,
                "What are you using for local Spring Boot observability?",
                "I want something lighter than a full hosted stack for local work. Curious what people keep running all the time versus only enabling during debugging.",
                42,
                LocalDateTime.now().minusDays(9)
        );
        Comment p1c1 = commentService.createTopLevel(p1.getId(), priya.getId(), "Micrometer plus Prometheus in Docker is still the sweet spot for me.");
        Comment p1c2 = commentService.createTopLevel(p1.getId(), marcus.getId(), "For solo work I often start with just actuator, structured logs, and a couple of custom timers.");
        commentService.createReply(p1c2.getId(), ava.getId(), "That is probably the right baseline. I keep overbuilding this piece.");
        commentService.createReply(p1c1.getId(), noah.getId(), "Same here, and Grafana dashboards are easy to copy between projects.");

        Post p2 = createPost(
                community,
                priya,
                "Anyone moved from JPA entities to jOOQ for a side project?",
                "I like Spring Data for CRUD, but once reporting queries pile up I start fighting the abstraction. Wondering where other people draw the line.",
                57,
                LocalDateTime.now().minusDays(7)
        );
        Comment p2c1 = commentService.createTopLevel(p2.getId(), ava.getId(), "I keep JPA for writes and use jOOQ for read-heavy reporting. Mixed setups can stay pretty sane.");
        Comment p2c2 = commentService.createTopLevel(p2.getId(), marcus.getId(), "The switch felt worth it once I had three different projections for the same report page.");
        Comment p2c3 = commentService.createTopLevel(p2.getId(), noah.getId(), "For a small app, I would delay it until the pain is consistent and obvious.");
        commentService.createReply(p2c1.getId(), priya.getId(), "That hybrid split is exactly what I was thinking.");
        commentService.createReply(p2c1.getId(), marcus.getId(), "It also makes performance tuning less mysterious.");
        Comment p2c2r1 = commentService.createReply(p2c2.getId(), ava.getId(), "Did you keep the same transaction boundaries in the service layer?");
        commentService.createReply(p2c2r1.getId(), marcus.getId(), "Yes, that part barely changed. Mostly repository code moved.");
        commentService.createReply(p2c3.getId(), priya.getId(), "Fair point. I might be optimizing for future pain instead of present pain.");

        createPost(
                community,
                noah,
                "Hot take: Flyway scripts are easier to review than generated diffs",
                "I know generated migrations save time, but every time something breaks in staging I wish the SQL had been written more explicitly.",
                33,
                LocalDateTime.now().minusDays(5)
        );

        Post p4 = createPost(
                community,
                marcus,
                "Show me your favorite tiny Spring Boot productivity trick",
                "Not architecture. Not cloud. Just the small thing that saves you five minutes every day.",
                71,
                LocalDateTime.now().minusDays(4)
        );
        Comment p4c1 = commentService.createTopLevel(p4.getId(), ava.getId(), "Keeping a scratch HTTP file beside the controller instead of hopping into Postman.");
        Comment p4c2 = commentService.createTopLevel(p4.getId(), priya.getId(), "Using test slices aggressively so I do not boot the whole app just to verify serialization.");
        Comment p4c3 = commentService.createTopLevel(p4.getId(), noah.getId(), "A dedicated dev profile that disables every noisy integration by default.");
        commentService.createReply(p4c1.getId(), marcus.getId(), "That one changed how fast I iterate on APIs.");
        commentService.createReply(p4c2.getId(), ava.getId(), "Same. It makes writing tests feel less like paying tax.");
        commentService.createReply(p4c3.getId(), priya.getId(), "I need to clean up my dev profile. It has become a junk drawer.");
        commentService.createReply(p4c3.getId(), marcus.getId(), "If it keeps Kafka from booting locally, it is already winning.");

        Post p5 = createPost(
                community,
                ava,
                "What belongs in a first-pass REST API and what should wait?",
                "I am helping a friend ship an MVP and trying to keep us from gold plating pagination, filtering, and every edge case before users exist.",
                64,
                LocalDateTime.now().minusDays(2)
        );
        Comment p5c1 = commentService.createTopLevel(p5.getId(), priya.getId(), "Stable identifiers, consistent errors, and enough filtering to keep the UI simple. Most other things can wait.");
        Comment p5c2 = commentService.createTopLevel(p5.getId(), noah.getId(), "Ship the endpoints your UI needs now, but leave room for future query parameters.");
        Comment p5c3 = commentService.createTopLevel(p5.getId(), marcus.getId(), "I would add basic pagination early if any list could realistically grow beyond a screenful.");
        commentService.createReply(p5c1.getId(), ava.getId(), "That is a useful boundary. I keep wanting to solve admin use cases too soon.");
        commentService.createReply(p5c1.getId(), marcus.getId(), "Consistent errors buy a surprising amount of frontend speed.");
        commentService.createReply(p5c2.getId(), priya.getId(), "Exactly. Reserve names now even if you do not implement every filter yet.");
        Comment p5c3r1 = commentService.createReply(p5c3.getId(), noah.getId(), "Cursor or offset for the first version?");
        commentService.createReply(p5c3r1.getId(), marcus.getId(), "Offset first unless the feed semantics are central to the product.");
        commentService.createReply(p5c3.getId(), ava.getId(), "That seems like the pragmatic call here.");
    }

    private void createFoodPosts(Community community, User ava, User marcus, User priya, User noah) {
        Post p1 = createPost(
                community,
                priya,
                "Weeknight dinner that feels impressive but takes under 30 minutes",
                "I need something that looks like effort when friends drop by, but is still realistic after work.",
                88,
                LocalDateTime.now().minusDays(8)
        );
        Comment p1c1 = commentService.createTopLevel(p1.getId(), ava.getId(), "Miso butter pasta with mushrooms. It tastes like you thought about it all day.");
        Comment p1c2 = commentService.createTopLevel(p1.getId(), noah.getId(), "Crispy chickpeas, yogurt, herbs, flatbread. Assembly-based meals are underrated.");
        commentService.createReply(p1c1.getId(), priya.getId(), "This is exactly the kind of answer I wanted.");
        commentService.createReply(p1c2.getId(), marcus.getId(), "Add roasted carrots and it suddenly looks restaurant-adjacent.");

        createPost(
                community,
                ava,
                "Is there a bread machine worth buying if I only bake twice a month?",
                "I love fresh bread, but I do not want another appliance that becomes a shelf monument.",
                26,
                LocalDateTime.now().minusDays(6)
        );

        Post p3 = createPost(
                community,
                marcus,
                "Best soup for feeding a crowd on a rainy day",
                "This weekend is supposed to be gray and I want the house to smell like something slow and generous.",
                49,
                LocalDateTime.now().minusDays(5)
        );
        Comment p3c1 = commentService.createTopLevel(p3.getId(), priya.getId(), "Lentil soup with lemon and cumin. Cheap, forgiving, and scales beautifully.");
        Comment p3c2 = commentService.createTopLevel(p3.getId(), ava.getId(), "White bean soup with parmesan rind if you want something richer.");
        Comment p3c3 = commentService.createTopLevel(p3.getId(), noah.getId(), "Chicken tortilla soup vanishes every time I make it for groups.");
        commentService.createReply(p3c1.getId(), marcus.getId(), "Lemon at the end or during the simmer?");
        commentService.createReply(p3c1.getId(), priya.getId(), "At the end. Keep it bright.");
        commentService.createReply(p3c2.getId(), noah.getId(), "Parmesan rind is one of those things that feels like cheating.");

        Post p4 = createPost(
                community,
                noah,
                "Tell me the one pantry ingredient you buy the expensive version of",
                "Mine is olive oil. It makes boring lunches feel less like damage control.",
                61,
                LocalDateTime.now().minusDays(3)
        );
        Comment p4c1 = commentService.createTopLevel(p4.getId(), ava.getId(), "Soy sauce. The gap between decent and excellent is huge.");
        Comment p4c2 = commentService.createTopLevel(p4.getId(), priya.getId(), "Canned tomatoes. Bad ones flatten an entire sauce.");
        Comment p4c3 = commentService.createTopLevel(p4.getId(), marcus.getId(), "Peanut butter. I can justify a lot when it fixes breakfasts for a month.");
        commentService.createReply(p4c1.getId(), noah.getId(), "Strong agreement. The cheap bottle only exists for marinades in my kitchen.");
        commentService.createReply(p4c2.getId(), ava.getId(), "This is the hill I will die on.");
        commentService.createReply(p4c3.getId(), priya.getId(), "Unexpected answer, but I respect it.");
        commentService.createReply(p4c3.getId(), marcus.getId(), "I said what I said.");

        Post p5 = createPost(
                community,
                priya,
                "What do you bring to a potluck when you do not know the crowd?",
                "I want something broadly liked, easy to transport, and not the fifteenth bowl of hummus.",
                73,
                LocalDateTime.now().minusDays(1)
        );
        Comment p5c1 = commentService.createTopLevel(p5.getId(), ava.getId(), "Sesame noodle salad. Travels well and tastes good warm or cold.");
        Comment p5c2 = commentService.createTopLevel(p5.getId(), marcus.getId(), "Brownies. There is freedom in bringing dessert.");
        Comment p5c3 = commentService.createTopLevel(p5.getId(), noah.getId(), "A grain salad with herbs, feta, and roasted vegetables covers a lot of dietary ground.");
        Comment p5c4 = commentService.createTopLevel(p5.getId(), priya.getId(), "I once brought marinated beans and ended up texting the recipe to six people.");
        commentService.createReply(p5c1.getId(), priya.getId(), "That might be the winner.");
        commentService.createReply(p5c2.getId(), ava.getId(), "Dessert is strategically brilliant.");
        commentService.createReply(p5c3.getId(), marcus.getId(), "Also resilient if it sits out longer than planned.");
        commentService.createReply(p5c4.getId(), noah.getId(), "Bean dishes deserve a reputation recovery.");
        commentService.createReply(p5c4.getId(), ava.getId(), "Please post that recipe next.");
    }

    private void createBuilderPosts(Community community, User ava, User marcus, User priya, User noah) {
        Post p1 = createPost(
                community,
                marcus,
                "Show me your favorite one-day project that actually improved your house",
                "Not the glamorous before-and-after stuff. The boring fix you still appreciate six months later.",
                67,
                LocalDateTime.now().minusDays(10)
        );
        Comment p1c1 = commentService.createTopLevel(p1.getId(), noah.getId(), "Adding proper pegboard storage in the utility closet. I stopped losing every small tool I own.");
        Comment p1c2 = commentService.createTopLevel(p1.getId(), ava.getId(), "Weatherstripping two leaky doors. It was instantly noticeable.");
        commentService.createReply(p1c1.getId(), marcus.getId(), "The unglamorous fixes are always the best return.");
        commentService.createReply(p1c2.getId(), priya.getId(), "This is my reminder to stop ignoring our back door.");

        Post p2 = createPost(
                community,
                noah,
                "Beginner woodworking project that teaches the right habits",
                "I have basic tools and enthusiasm, but not enough experience to tell the difference between a fun first build and a discouraging one.",
                54,
                LocalDateTime.now().minusDays(7)
        );
        Comment p2c1 = commentService.createTopLevel(p2.getId(), marcus.getId(), "A simple shop stool. Straight cuts, repeatable measurements, and you will actually use it.");
        Comment p2c2 = commentService.createTopLevel(p2.getId(), ava.getId(), "Wall shelves taught me more patience than anything else.");
        Comment p2c3 = commentService.createTopLevel(p2.getId(), priya.getId(), "A planter box if you want some forgiveness while learning.");
        commentService.createReply(p2c1.getId(), noah.getId(), "That sounds less intimidating than a coffee table.");
        commentService.createReply(p2c2.getId(), marcus.getId(), "Shelves also expose every tiny leveling mistake immediately.");
        commentService.createReply(p2c3.getId(), ava.getId(), "And outdoor projects hide cosmetic flaws better.");
        commentService.createReply(p2c3.getId(), noah.getId(), "That is exactly the level of mercy I need.");

        createPost(
                community,
                priya,
                "What is the best fix you learned from a grandparent or neighbor?",
                "I am collecting the sort of small practical knowledge that never shows up in product manuals.",
                39,
                LocalDateTime.now().minusDays(4)
        );

        Post p4 = createPost(
                community,
                ava,
                "How do you keep a weekend project from becoming a three-month guilt pile?",
                "My pattern is overbuy materials, underestimate prep, and then avoid the mess for weeks.",
                82,
                LocalDateTime.now().minusDays(3)
        );
        Comment p4c1 = commentService.createTopLevel(p4.getId(), marcus.getId(), "I define a hard stopping point before I start, even if the project is unfinished.");
        Comment p4c2 = commentService.createTopLevel(p4.getId(), noah.getId(), "Prep night on Friday. Actual build on Saturday. Cleanup before dinner no matter what.");
        Comment p4c3 = commentService.createTopLevel(p4.getId(), priya.getId(), "I started splitting design decisions from build time. It helps a lot.");
        Comment p4c4 = commentService.createTopLevel(p4.getId(), ava.getId(), "I may need to print this thread and tape it to my garage wall.");
        commentService.createReply(p4c1.getId(), ava.getId(), "That is probably the discipline I am missing.");
        commentService.createReply(p4c2.getId(), marcus.getId(), "Prep night is underrated. It protects the fun part.");
        commentService.createReply(p4c3.getId(), noah.getId(), "Decision fatigue is real on DIY projects too.");
        commentService.createReply(p4c4.getId(), priya.getId(), "If you do, make it level.");
        commentService.createReply(p4c4.getId(), marcus.getId(), "And anchor it into a stud.");
        commentService.createReply(p4c4.getId(), noah.getId(), "Now this thread has become extremely on brand.");

        Post p5 = createPost(
                community,
                marcus,
                "Small garage upgrades that made the space easier to work in",
                "Lighting, layout, airflow, storage, whatever changed the room from annoying to usable.",
                58,
                LocalDateTime.now().minusHours(36)
        );
        Comment p5c1 = commentService.createTopLevel(p5.getId(), ava.getId(), "LED shop lights were the biggest quality-of-life upgrade per dollar.");
        Comment p5c2 = commentService.createTopLevel(p5.getId(), noah.getId(), "A fan pointed across the bench instead of at me. Much better for dust and summer heat.");
        Comment p5c3 = commentService.createTopLevel(p5.getId(), priya.getId(), "Rolling carts. I resisted them for too long.");
        commentService.createReply(p5c1.getId(), marcus.getId(), "Same answer here. Bad lighting makes every task feel harder.");
        commentService.createReply(p5c2.getId(), ava.getId(), "That cross-breeze detail is smart.");
        commentService.createReply(p5c3.getId(), noah.getId(), "Anything on wheels becomes twice as useful in a garage.");
    }
}