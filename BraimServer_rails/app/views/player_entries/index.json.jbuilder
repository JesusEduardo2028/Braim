json.array!(@player_entries) do |player_entry|
  json.extract! player_entry, :id
  json.url player_entry_url(player_entry, format: :json)
end
