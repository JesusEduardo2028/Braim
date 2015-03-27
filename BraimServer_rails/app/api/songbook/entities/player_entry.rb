module Songbook
  module Entities
    class PlayerEntry < Grape::Entity
      expose :timestamp, documentation: { type: "float", desc: "timestamp", example: "1418665475068.0" }
      expose :song_id, documentation: { type: "string", desc: "id of the song", example: '172a66834fb7802c28000003' } do |player_entry, options|
        player_entry.song_id.to_s
      end
      expose :action, documentation: { type: "symbol", desc: "action made by the user", example: "pause" }
    end
  end
end

