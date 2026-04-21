import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import Home from './pages/Home'
import CommunityPage from './pages/CommunityPage'
import PostPage from './pages/PostPage'
import ProfilePage from './pages/ProfilePage'

const CURRENT_USER = 'ava'

function App() {
  return (
    <BrowserRouter>
      <nav style={{ background: '#ff4500', padding: '10px 20px', display: 'flex', alignItems: 'center', gap: 20 }}>
        <Link to="/" style={{ color: 'white', fontWeight: 'bold', fontSize: 20 }}>Comunatee</Link>
        <div style={{ marginLeft: 'auto' }}>
          <Link to={`/u/${CURRENT_USER}`} style={{ color: 'white', fontWeight: 'bold', fontSize: 14, background: 'rgba(255,255,255,0.2)', padding: '6px 14px', borderRadius: 4 }}>
            u/{CURRENT_USER}
          </Link>
        </div>
      </nav>
      <div style={{ maxWidth: 900, margin: '20px auto', padding: '0 16px' }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/c/:communitySlug" element={<CommunityPage />} />
          <Route path="/c/:communitySlug/post/:postId" element={<PostPage />} />
          <Route path="/c/:communitySlug/p/:postSlug" element={<PostPage />} />
          <Route path="/post/:postId" element={<PostPage />} />
          <Route path="/u/:username" element={<ProfilePage />} />
        </Routes>
      </div>
    </BrowserRouter>
  )
}

export default App
