import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import Home from './pages/Home'
import CommunityPage from './pages/CommunityPage'
import PostPage from './pages/PostPage'

function App() {
  return (
    <BrowserRouter>
      <nav style={{ background: '#ff4500', padding: '10px 20px', display: 'flex', alignItems: 'center', gap: 20 }}>
        <Link to="/" style={{ color: 'white', fontWeight: 'bold', fontSize: 20 }}>Comunatee</Link>
      </nav>
      <div style={{ maxWidth: 900, margin: '20px auto', padding: '0 16px' }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/c/:communityId" element={<CommunityPage />} />
          <Route path="/post/:postId" element={<PostPage />} />
        </Routes>
      </div>
    </BrowserRouter>
  )
}

export default App
