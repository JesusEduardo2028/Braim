# config valid only for Capistrano 3.1


set :git_strategy, RemoteCacheWithProjectRootStrategy
set :project_root, 'BraimServer_rails'

set :application, 'Braim'
set :repo_url, 'git@github.com:JesusEduardo2028/Braim.git'

set :deploy_to, '/home/deploy/code/BraimServer_rails'

set :linked_files, %w{config/mongoid.yml config/secrets.yml}
set :linked_dirs, %w{bin log tmp/pids tmp/cache tmp/sockets vendor/bundle public/system public/uploads}

namespace :deploy do

  desc 'Restart application'
  task :restart do
    on roles(:app), in: :sequence, wait: 5 do
      execute :touch, release_path.join('tmp/restart.txt')
    end
  end

  after :publishing, 'deploy:restart'
  after :finishing, 'deploy:cleanup'
end
