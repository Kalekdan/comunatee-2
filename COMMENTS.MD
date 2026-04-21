# Comments Guide
The appraoch for structuring and nesting comments is given here

#  Comment Threading (CRITICAL)

## Concept

Each comment stores:

* `parentId` → direct parent
* `path` → full ancestry chain
* `depth` → nesting level

---

## Example Thread

```id="x9tfq2"
Post
 ├── A
 │    ├── B
 │    │    └── C
 │    └── D
```

---

## Stored Data

| Comment | parent_id | path    | depth |
| ------- | --------- | ------- | ----- |
| A       | null      | "1"     | 0     |
| B       | A         | "1.2"   | 1     |
| C       | B         | "1.2.3" | 2     |
| D       | A         | "1.4"   | 1     |

---

# Creating Comments

## 1. Top-Level Comment

```java id="b8y6du"
Comment c = new Comment();
c.setPost(post);
c.setParentId(null);
c.setDepth(0);

commentRepository.save(c);

// AFTER ID exists
c.setPath(String.valueOf(c.getId()));
commentRepository.save(c);
```

---

## 2. Reply to a Comment

```java id="r2r4mt"
Comment parent = commentRepository.findById(parentId).get();

Comment c = new Comment();
c.setPost(parent.getPost());
c.setParentId(parent.getId());
c.setDepth(parent.getDepth() + 1);

commentRepository.save(c);

// Build path AFTER save
c.setPath(parent.getPath() + "." + c.getId());
commentRepository.save(c);
```

---

## Depth Limit

```java id="2yxyrc"
if (parent.getDepth() >= 10) {
    throw new RuntimeException("Max depth reached");
}
```

---

# Fetching Comments

```java id="i4w0l9"
List<Comment> comments =
    commentRepository.findByPostIdOrderByPath(postId);
```

This returns comments in correct tree order automatically.

---

# Deleting Comments

Never delete rows:

```java id="d1xv7m"
comment.setBody("[deleted]");
comment.setDeleted(true);
```

---
