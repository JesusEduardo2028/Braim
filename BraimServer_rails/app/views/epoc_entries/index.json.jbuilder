json.array!(@epoc_entries) do |epoc_entry|
  json.extract! epoc_entry, :id
  json.url epoc_entry_url(epoc_entry, format: :json)
end
