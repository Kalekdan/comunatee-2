import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import API from '../api'
import VoteButtons from '../components/VoteButtons'
import CommentTree from '../components/CommentTree'

export default function PostPage() {
  const { postId } = useParams()
  const [post, setPost] = useState(null)
  const [comments, setComments] = useState([])
  const [newComment, setNewComment] = useState('')

  const MOCK_USER_ID = 1 // In a real app this would come from auth context

  useEffect(() => {
    API.get(`/posts/${postId}`).then(r => setPost(r.data))
    API.get(`/comments/post/${postId}`).then(r => setComments(r.data))
  }, [postId])

  const handleVotePost = (voteType) => {
    API.post(`/posts/${postId}/vote`, { userId: MOCK_USER_ID, voteType })
      .then(r => setPost(r.data))
  }

  const handleSubmitComment = (e) => {
    e.preventDefault()
    API.post('/comments', { postId: Number(postId), authorId: MOCK_USER_ID, body: newComment })
      .then(r => {
        setComments(prev => [...prev, r.data])
        setNewComment('')
      })
  }

  const handleReply = (parentId, body) => {
    return API.post('/comments', { parentId, authorId: MOCK_USER_ID, body })
      .then(r => {
        setComments(prev => [...prev, r.data])
        return r.data
      })
  }

  const handleVoteComment = (commentId, voteType) => {
    API.post(`/comments/${commentId}/vote`, { userId: MOCK_USER_ID, voteType })
      .then(r => setComments(prev => prev.map(c => c.id === r.data.id ? r.data : c)))
  }

  if (!post) return <p>Loading...</p>

  return (
    <div>
      <div style={{ background: 'white', borderRadius: 4, padding: 16, marginBottom: 16 }}>
        <div style={{ display: 'flex', gap: 12, alignItems: 'flex-start' }}>
          <VoteButtons score={post.score} onVote={handleVotePost} />
          <div>
            <h2>{post.title}</h2>
            <small>posted by u/{post.author?.username} in c/{post.community?.name}</small>
            <p style={{ marginTop: 12 }}>{post.body}</p>
            <small>{post.commentCount} comments</small>
          </div>
        </div>
      </div>

      <div style={{ background: 'white', borderRadius: 4, padding: 16, marginBottom: 16 }}>
        <h3>Leave a comment</h3>
        <form onSubmit={handleSubmitComment} style={{ display: 'flex', flexDirection: 'column', gap: 8, marginTop: 8 }}>
          <textarea
            value={newComment}
            onChange={e => setNewComment(e.target.value)}
            rows={3}
            placeholder="What are your thoughts?"
            style={{ padding: 8, borderRadius: 4, border: '1px solid #ccc', resize: 'vertical' }}
            required
          />
          <button type="submit" style={{ alignSelf: 'flex-end', padding: '6px 16px', background: '#ff4500', color: 'white', border: 'none', borderRadius: 4, cursor: 'pointer' }}>
            Comment
          </button>
        </form>
      </div>

      <CommentTree comments={comments} onReply={handleReply} onVote={handleVoteComment} />
    </div>
  )
}
