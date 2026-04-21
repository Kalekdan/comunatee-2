import { useState } from 'react'
import VoteButtons from './VoteButtons'

export default function CommentCard({ comment, onReply, onVote, children }) {
  const [replying, setReplying] = useState(false)
  const [replyBody, setReplyBody] = useState('')

  const handleReply = (e) => {
    e.preventDefault()
    onReply(comment.id, replyBody).then(() => {
      setReplyBody('')
      setReplying(false)
    })
  }

  return (
    <div style={{ marginLeft: comment.depth * 20, borderLeft: '2px solid #eee', paddingLeft: 12, marginBottom: 12 }}>
      <div style={{ display: 'flex', gap: 8, alignItems: 'flex-start' }}>
        <VoteButtons score={comment.score} onVote={(v) => onVote(comment.id, v)} />
        <div style={{ flex: 1 }}>
          <small style={{ color: '#888' }}>u/{comment.author?.username} · depth {comment.depth}</small>
          <p style={{ margin: '4px 0' }}>{comment.deleted ? <em>[deleted]</em> : comment.body}</p>
          <button
            onClick={() => setReplying(!replying)}
            style={{ background: 'none', border: 'none', cursor: 'pointer', color: '#0079d3', fontSize: 12, padding: 0 }}
          >
            {replying ? 'Cancel' : 'Reply'}
          </button>
          {replying && (
            <form onSubmit={handleReply} style={{ marginTop: 8, display: 'flex', flexDirection: 'column', gap: 4 }}>
              <textarea
                value={replyBody}
                onChange={e => setReplyBody(e.target.value)}
                rows={2}
                placeholder="Your reply..."
                style={{ padding: 6, borderRadius: 4, border: '1px solid #ccc', resize: 'vertical' }}
                required
              />
              <button type="submit" style={{ alignSelf: 'flex-end', padding: '4px 12px', background: '#ff4500', color: 'white', border: 'none', borderRadius: 4, cursor: 'pointer' }}>
                Reply
              </button>
            </form>
          )}
          {children}
        </div>
      </div>
    </div>
  )
}
