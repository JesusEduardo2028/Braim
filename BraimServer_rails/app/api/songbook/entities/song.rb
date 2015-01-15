module Songbook
  module Entities
    class Song < Grape::Entity
      expose :id, documentation: { type: "string", desc: "id of the entry", example: '172a66834fb7802c28000003' } do |song, options|
        song.id.to_s
      end
      expose :name, documentation: { type: "string", desc: "song name", example: "All my life" }
      expose :duration, documentation: { type: "string", desc: "song duration", example: "300000" }
      expose :url, documentation: { type: "string", desc: "song url", example: "http://braimserver/songs/all_my_life.mp3" } do |song,options|
        song.file_url
      end
    end
  end
end
