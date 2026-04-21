import { Link } from 'react-router-dom'
import { getCommunityPath, getPostPath, getUserPath } from '../utils/routes'

export default function PostCard({ post }) {
  return (
    <div style={{ background: 'white', borderRadius: 4, padding: 16, marginBottom: 8, display: 'flex', gap: 12 }}>
      <div style={{ textAlign: 'center', minWidth: 40 }}>
        <div style={{ fontWeight: 'bold' }}>{post.score}</div>
        <div style={{ fontSize: 11, color: '#888' }}>points</div>
      </div>
      <div>
        <Link to={getPostPath(post)} style={{ fontWeight: 'bold', fontSize: 16 }}>{post.title}</Link>
        <div style={{ fontSize: 12, color: '#888', marginTop: 4 }}>
          <Link to={getCommunityPath(post.community)}>c/{post.community?.name}</Link>
          {' · '}<Link to={getUserPath(post.author)}>u/{post.author?.username}</Link>
          {' · '}
          {post.commentCount} comments
        </div>
      </div>
    </div>
  )
}
