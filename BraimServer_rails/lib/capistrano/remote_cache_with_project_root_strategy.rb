# Usage: 
# 1. Drop this file into lib/capistrano/remote_cache_with_project_root_strategy.rb
# 2. Add the following to your Capfile:
#   require 'capistrano/git'
#   require './lib/capistrano/remote_cache_with_project_root_strategy'
# 3. Add the following to your config/deploy.rb
#    set :git_strategy, RemoteCacheWithProjectRootStrategy
#    set :project_root, 'subdir/path'
 
# Define a new SCM strategy, so we can deploy only a subdirectory of our repo.
module RemoteCacheWithProjectRootStrategy
  include Capistrano::Git::DefaultStrategy
  def test
    test! " [ -f #{repo_path}/HEAD ] "
  end
 
  def check
    test! :git, :'ls-remote -h', repo_url
  end
 
  def clone
    git :clone, '--mirror', repo_url, repo_path
  end
 
  def update
    git :remote, :update
  end
 
  def release
    git :archive, fetch(:branch), fetch(:project_root), '| tar -x -C', release_path, "--strip=#{fetch(:project_root).count('/')+1}"
  end
end
