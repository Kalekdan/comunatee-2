import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import API from '../api'
import VoteButtons from '../components/VoteButtons'
import CommentTree from '../components/CommentTree'
import { extractPostId, getCommunityPath, getUserPath } from '../utils/routes'

export default function PostPage() {
  const { postId, postSlug } = useParams()
  const [post, setPost] = useState(null)
  const [comments, setComments] = useState([])
  const [newComment, setNewComment] = useState('')
  const [loadError, setLoadError] = useState(false)

  const MOCK_USER_ID = 1 // In a real app this would come from auth context
  const resolvedPostId = extractPostId(postSlug || postId)

  useEffect(() => {
    if (!resolvedPostId) {
      setLoadError(true)
      return
    }

    setLoadError(false)
    API.get(`/posts/${resolvedPostId}`).then(r => setPost(r.data))
    API.get(`/comments/post/${resolvedPostId}`).then(r => setComments(r.data))
  }, [resolvedPostId])

  const handleVotePost = (voteType) => {
    API.post(`/posts/${resolvedPostId}/vote`, { userId: MOCK_USER_ID, voteType })
      .then(r => setPost(r.data))
  }

  const handleSubmitComment = (e) => {
    e.preventDefault()
    API.post('/comments', { postId: resolvedPostId, authorId: MOCK_USER_ID, body: newComment })
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

  if (loadError) return <p>Post not found.</p>

  if (!post) return <p>Loading...</p>

  return (
    <div>
      <div style={{ background: 'white', borderRadius: 4, padding: 16, marginBottom: 16 }}>
        <div style={{ display: 'flex', gap: 12, alignItems: 'flex-start' }}>
          <VoteButtons score={post.score} onVote={handleVotePost} />
          <div>
            <h2>{post.title}</h2>
            <small>
              posted by <Link to={getUserPath(post.author)}>u/{post.author?.username}</Link> in <Link to={getCommunityPath(post.community)}>c/{post.community?.name}</Link>
            </small>
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
