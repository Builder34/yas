/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  output: 'standalone',
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://storefront.yas.local.com/api/:path*',
      },
      {
        source: '/authentication/:path*',
        destination: 'http://storefront.yas.local.com/authentication/:path*',
      },
      {
        source: '/oauth2/:path*',
        destination: 'http://storefront.yas.local.com/oauth2/:path*',
      },
      {
        source: '/realms/:path*',
        destination: 'http://identity.yas.local.com/realms/:path*',
      },
      {
        source: '/login/:path*',
        destination: 'http://identity.yas.local.com/login/:path*',
      },
    ];
  },
  images: {
    remotePatterns: [
      {
        hostname: 'api.yas.local',
      },
    ],
  },
};

module.exports = nextConfig;
