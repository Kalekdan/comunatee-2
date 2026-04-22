import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import API from '../api'
import { getPostPath, getCommunityPath } from '../utils/routes'

export default function ProfilePage() {
  const { username } = useParams()
  const [user, setUser] = useState(null)
  const [posts, setPosts] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    API.get(`/users/by-username/${encodeURIComponent(username)}`)
      .then(r => {
        setUser(r.data)
        return API.get(`/users/${r.data.id}/posts`)
      })
      .then(r => setPosts(r.data))
      .catch(() => setError('User not found'))
  }, [username])

  if (error) return <p>{error}</p>
  if (!user) return <p>Loading...</p>

  return (
    <div>
      <div style={{ background: 'white', borderRadius: 4, padding: 20, marginBottom: 16, display: 'flex', gap: 20, alignItems: 'flex-start' }}>
        {user.profilePicUrl && (
          <img
            src={user.profilePicUrl}
            alt={user.username}
            style={{ width: 100, height: 100, borderRadius: '50%', objectFit: 'cover', flexShrink: 0 }}
          />
        )}
        <div>
          <h2 style={{ margin: 0 }}>u/{user.username}</h2>
          <div style={{ color: '#888', fontSize: 13, marginTop: 8, display: 'flex', gap: 24 }}>
            <span>Post karma: <strong>{user.postRating}</strong></span>
            <span>Comment karma: <strong>{user.commentRating}</strong></span>
          </div>
        </div>
      </div>

      <h3>Posts</h3>
      {posts.length === 0 && <p style={{ color: '#888' }}>No posts yet.</p>}
      {posts.map(post => (
        <div key={post.id} style={{ background: 'white', borderRadius: 4, padding: 16, marginBottom: 8, display: 'flex', gap: 12 }}>
          <div style={{ textAlign: 'center', minWidth: 40 }}>
            <div style={{ fontWeight: 'bold' }}>{post.score}</div>
            <div style={{ fontSize: 11, color: '#888' }}>points</div>
          </div>
          <div>
            <Link to={getPostPath(post)} style={{ fontWeight: 'bold', fontSize: 16 }}>{post.title}</Link>
            <div style={{ fontSize: 12, color: '#888', marginTop: 4 }}>
              <Link to={getCommunityPath(post.community)}>c/{post.community?.name}</Link>
              {' · '}{post.commentCount} comments
            </div>
          </div>
        </div>
      ))}
    </div>
  )
}
