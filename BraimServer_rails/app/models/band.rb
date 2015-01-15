class Band
  include Mongoid::Document
  field :name, type: String
  field :genre, type: String
  field :biography, type: String

  has_many :albums
end
