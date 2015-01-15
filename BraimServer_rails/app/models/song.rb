class Song
  include Mongoid::Document
  mount_uploader :file, FileUploader

  field :name, type: String
  field :duration, type: Integer
  field :file, type: String
  
  belongs_to :album
end
