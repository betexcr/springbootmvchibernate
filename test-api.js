#!/usr/bin/env node
const fetch = (...args) => import('node-fetch').then(({default: fetch}) => fetch(...args));

async function main() {
  const api = process.env.API_BASE || 'http://localhost:8080';
  const site = process.env.SITE_BASE || 'http://localhost:3010';

  // 1) Seed deterministic product
  const seedRes = await fetch(`${api}/e2e/seedProduct`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id: 999, name: 'E2E Test Product', unitPrice: 12.34 })
  });
  console.log('seed status', seedRes.status);
  if (!seedRes.ok) process.exit(1);

  // 2) Search API check
  const search = await fetch(`${api}/e2e/search?q=chai&size=2`).then(r => r.json());
  console.log('search count (api)', search.totalElements ?? 'n/a');

  // 3) Home page SSR fetch
  const home = await fetch(`${site}`).then(r => ({ status: r.status, text: r.text() }));
  console.log('home status', home.status);
  if (home.status !== 200) process.exit(2);

  // 4) Product details page SSR fetch for the seeded product
  const product = await fetch(`${site}/product/999-e2e-test-product`).then(r => ({ status: r.status }));
  console.log('product status', product.status);
  if (product.status !== 200) process.exit(3);

  console.log('E2E OK');
}

main().catch((e) => { console.error(e); process.exit(99); }); 