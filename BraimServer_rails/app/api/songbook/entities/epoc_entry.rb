module Songbook
  module Entities
    class EpocEntry < Grape::Entity
      expose :nodes, documentation:{type: "array", desc: "an array of float values", example: "[]" }
      expose :timestamp,documentation:{type: "float", desc: "timestamp", example: "timestamp example"}
      expose :excitement,documentation:{type: "string", desc: "excitement", example: "0.34"}
      expose :frustration,documentation:{type: "string", desc: "frustration", example: "0.55"}
      expose :meditation,documentation:{type: "string", desc: "meditation", example: "1"}
      expose :engagement,documentation:{type: "string", desc: "engagement", example: "0.0"}
    end
  end
end
