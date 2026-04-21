import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import API from '../api'
import PostCard from '../components/PostCard'
import { matchesCommunitySlug } from '../utils/routes'

export default function CommunityPage() {
  const { communitySlug } = useParams()
  const [community, setCommunity] = useState(null)
  const [posts, setPosts] = useState([])
  const [notFound, setNotFound] = useState(false)

  useEffect(() => {
    setNotFound(false)
    setCommunity(null)
    setPosts([])

    API.get('/communities').then(({ data }) => {
      const matchedCommunity = data.find(candidate => matchesCommunitySlug(candidate, communitySlug))

      if (!matchedCommunity) {
        setNotFound(true)
        return
      }

      setCommunity(matchedCommunity)
      return API.get(`/posts/community/${matchedCommunity.id}`).then(r => setPosts(r.data))
    })
  }, [communitySlug])

  if (notFound) return <p>Community not found.</p>

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
