import { Link } from 'react-router-dom'

export default function PostCard({ post }) {
  return (
    <div style={{ background: 'white', borderRadius: 4, padding: 16, marginBottom: 8, display: 'flex', gap: 12 }}>
      <div style={{ textAlign: 'center', minWidth: 40 }}>
        <div style={{ fontWeight: 'bold' }}>{post.score}</div>
        <div style={{ fontSize: 11, color: '#888' }}>points</div>
      </div>
      <div>
        <Link to={`/post/${post.id}`} style={{ fontWeight: 'bold', fontSize: 16 }}>{post.title}</Link>
        <div style={{ fontSize: 12, color: '#888', marginTop: 4 }}>
          <Link to={`/c/${post.community?.id}`}>c/{post.community?.name}</Link>
          {' · '}u/{post.author?.username}
          {' · '}
          {post.commentCount} comments
        </div>
      </div>
    </div>
  )
}
