namespace :db  do
  desc "Fill database with sample data uploading songs from server songs directory"

  task populate: :environment do
    User.create!(email: "test_user@fake.com", password: "password",username:"testuser")
  end
end