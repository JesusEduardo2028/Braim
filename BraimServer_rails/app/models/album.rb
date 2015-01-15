class Album
  include Mongoid::Document
  field :name, type: String
  field :release_date, type: Date

  belongs_to :band
  has_many :songs
end
