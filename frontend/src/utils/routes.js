export function slugify(value) {
  return String(value || '')
    .toLowerCase()
    .trim()
    .replace(/[^a-z0-9\s-]/g, '')
    .replace(/\s+/g, '-')
    .replace(/-+/g, '-')
}

export function getCommunitySlug(community) {
  if (!community) return ''
  return encodeURIComponent(community.name || String(community.id))
}

export function getCommunityPath(community) {
  return `/c/${getCommunitySlug(community)}`
}

export function getPostSlug(post) {
  return `${slugify(post?.title).slice(0, 80)}_${post?.id}`
}

export function getPostPath(post) {
  return `${getCommunityPath(post?.community)}/p/${encodeURIComponent(getPostSlug(post))}`
}

export function extractPostId(postParam) {
  const value = decodeURIComponent(postParam || '')
  const slugMatch = value.match(/_(\d+)$/)

  if (slugMatch) {
    return Number(slugMatch[1])
  }

  if (/^\d+$/.test(value)) {
    return Number(value)
  }

  return null
}

export function matchesCommunitySlug(community, communityParam) {
  const value = decodeURIComponent(communityParam || '')

  return String(community.id) === value || community.name === value || slugify(community.name) === slugify(value)
}