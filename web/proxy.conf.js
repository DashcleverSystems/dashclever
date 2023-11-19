const HttpsProxyAgent = require('https-proxy-agent');
const { env } = require('process');

const proxyConfig = [
  {
    context: '/api/**',
    target: env.npm_config_api_url,
    changeOrigin: true,
    ws: true,
    secure: false,
  },
];

function setupForCorporateProxy(proxyConfig) {
  if (!Array.isArray(proxyConfig)) {
    proxyConfig = [proxyConfig];
  }

  const proxyServer = process.env.http_proxy || process.env.HTTP_PROXY;
  let agent = null;

  if (proxyServer) {
    console.log(`Using corporate proxy server: ${proxyServer}`);
    agent = new HttpsProxyAgent(proxyServer);
    proxyConfig.forEach((entry) => {
      entry.agent = agent;
    });
  }

  return proxyConfig;
}

module.exports = setupForCorporateProxy(proxyConfig);
