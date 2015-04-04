module Songbook
  module Entities
    class SongFragment < Grape::Entity
      expose :song_id, documentation: { type: "string", desc: "song of the song", example: "song_id..." }
      expose :start_at, documentation: { type: "float", desc: "timestamp", example: "" }
      expose :end_at, documentation:  { type: "float", desc: "timestamp", example: "" }
    end
  end
end
