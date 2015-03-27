module Songbook
  module Entities
    class Session < Grape::Entity
      expose :id, documentation: { type: "string", desc: "id of the entry", example: '172a66834fb7802c28000003' } do |session, options|
        session.id.to_s
      end
      expose :start_at, documentation: { type: "float", desc: "timestamp that indicates when the session was started", example: "1418665475068.0" }
       expose :user_id, documentation: { type: "string", desc: "id of the user that create the session", example: '172a6683ffgb7802c28000003' } do |session, options|
        session.user.id.to_s
      end
    end
  end
end