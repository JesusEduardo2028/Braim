set :stage, :staging

# Replace 127.0.0.1 with your server's IP address!
server '104.40.72.171', user: 'deploy', roles: %w{web app}
