# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)
require 'csv'

puts 'creating users'

users = []
common_password = '12345678'

['elva.lazo@example.com', 'allam.britto@example.com' ].each do |email|
  users << User.find_or_create_by(email: email, password: common_password, password_confirmation: common_password)
end

puts 'creating seed songs'

CSV.foreach(File.join(Rails.root, 'public', 'res', 'songbook.csv'), headers: true) do |song|
  band = Band.find_or_create_by name: song['Band'], genre: song['Genre'], biography: 'short biography'
  album = band.albums.find_or_create_by name: song['Album'], release_date: Date.today
  album.songs.create name: song['Song'], duration: 300
end

puts 'seeding process has been finished.'
