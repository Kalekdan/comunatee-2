export default function VoteButtons({ score, onVote }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4, minWidth: 40 }}>
      <button onClick={() => onVote(1)} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 18 }}>▲</button>
      <span style={{ fontWeight: 'bold' }}>{score}</span>
      <button onClick={() => onVote(-1)} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 18 }}>▼</button>
    </div>
  )
}
