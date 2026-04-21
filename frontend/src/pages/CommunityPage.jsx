import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import API from '../api'
import PostCard from '../components/PostCard'

export default function CommunityPage() {
  const { communityId } = useParams()
  const [community, setCommunity] = useState(null)
  const [posts, setPosts] = useState([])

  useEffect(() => {
    API.get(`/communities/${communityId}`).then(r => setCommunity(r.data))
    API.get(`/posts/community/${communityId}`).then(r => setPosts(r.data))
  }, [communityId])

  if (!community) return <p>Loading...</p>

  return (
    <div>
      <div style={{ background: 'white', borderRadius: 4, padding: 16, marginBottom: 16 }}>
        <h2>c/{community.name}</h2>
        <p>{community.description}</p>
        <small>{community.subscriberCount} subscribers</small>
      </div>
      {posts.length === 0 && <p>No posts in this community yet.</p>}
      {posts.map(post => (
        <PostCard key={post.id} post={post} />
      ))}
    </div>
  )
}
