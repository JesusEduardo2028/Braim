module Songbook
  module Entities
    module Users
      class Default < Grape::Entity
        expose :email, documentation: { type: "email", desc: "user email", example: "allam_britto@example.com" }
        expose :sign_in_count, documentation: { type: "integer", desc: "sign in count", example: 3 }
        expose :current_sign_in_at, documentation: { type: "string", desc: "current sign in at...", example: "2014-11-12 13:44:37 -0500" }
      end
    end
  end
end
