/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  output: 'standalone',
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://backoffice.yas.local.com/api/:path*',
      },
      {
        source: '/authentication/:path*',
        destination: 'http://backoffice.yas.local.com/authentication/:path*',
      },
      {
        source: '/oauth2/:path*',
        destination: 'http://backoffice.yas.local.com/oauth2/:path*',
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
};

module.exports = nextConfig;
