import CommentCard from './CommentCard'

export default function CommentTree({ comments, onReply, onVote }) {
  if (!comments || comments.length === 0) return <p>No comments yet.</p>

  return (
    <div>
      {comments.map(comment => (
        <CommentCard
          key={comment.id}
          comment={comment}
          onReply={onReply}
          onVote={onVote}
        />
      ))}
    </div>
  )
}
