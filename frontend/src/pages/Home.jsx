import { useEffect, useState } from 'react'
import API from '../api'
import PostCard from '../components/PostCard'

export default function Home() {
  const [posts, setPosts] = useState([])

  useEffect(() => {
    API.get('/posts').then(r => setPosts(r.data))
  }, [])

  return (
    <div>
      <h2 style={{ marginBottom: 16 }}>All Posts</h2>
      {posts.length === 0 && <p>No posts yet.</p>}
      {posts.map(post => (
        <PostCard key={post.id} post={post} />
      ))}
    </div>
  )
}
