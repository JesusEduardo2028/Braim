namespace :db  do
  desc "Fill database with sample data uploading songs from server songs directory"

  task populate: :environment do
    User.create!(email: "test_user@fake.com", password: "password",username:"testuser")
    Dir.glob('/home/jesus/tesis/songs/*').each do|file|
        name = file.split('/').last
        song =Song.create!(name: name)
        song.file.store!(File.open(File.join(file)))
        song.save!
    end
    puts "#{Song.all.count} uploaded songs in the database !"
  end
end