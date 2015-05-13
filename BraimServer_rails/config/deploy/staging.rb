set :stage, :staging

# Replace 127.0.0.1 with your server's IP address!
server 'braim-stag.cloudapp.net', user: 'deploy', roles: %w{web app}
